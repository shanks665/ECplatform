// API Base URL
const API_BASE_URL = '/api';

// グローバル変数
let currentUser = null;
let cart = [];

// ページ読み込み時の初期化
document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
    loadCategories();
    loadProducts('all');
    updateCartCount();
});

// 認証状態の確認
function checkAuth() {
    const token = localStorage.getItem('authToken');
    const user = localStorage.getItem('user');
    
    if (token && user) {
        currentUser = JSON.parse(user);
        document.getElementById('auth-link').textContent = currentUser.username;
        document.getElementById('auth-link').href = '#';
        document.getElementById('auth-link').onclick = logout;
    }
}

// ログアウト
function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
    currentUser = null;
    window.location.reload();
}

// カテゴリー読み込み
async function loadCategories() {
    try {
        const response = await fetch(`${API_BASE_URL}/categories`);
        const result = await response.json();
        
        if (result.success && result.data) {
            displayCategories(result.data);
        }
    } catch (error) {
        console.error('カテゴリー読み込みエラー:', error);
    }
}

// カテゴリー表示
function displayCategories(categories) {
    const categoryList = document.getElementById('category-list');
    categoryList.innerHTML = '';
    
    const icons = {
        'Electronics': '💻',
        'Clothing': '👕',
        'Books': '📚',
        'Home & Kitchen': '🏠',
        'Sports & Outdoors': '⚽'
    };
    
    categories.slice(0, 5).forEach(category => {
        const card = document.createElement('div');
        card.className = 'category-card';
        card.onclick = () => loadProductsByCategory(category.id);
        
        card.innerHTML = `
            <div class="category-icon">${icons[category.name] || '📦'}</div>
            <h3>${category.name}</h3>
            <p>${category.productCount || 0} 商品</p>
        `;
        
        categoryList.appendChild(card);
    });
}

// 商品読み込み
async function loadProducts(type = 'all') {
    showLoading(true);
    
    try {
        let url = `${API_BASE_URL}/products`;
        
        if (type === 'featured') {
            url = `${API_BASE_URL}/products/featured`;
        } else if (type === 'sale') {
            url = `${API_BASE_URL}/products/on-sale`;
        }
        
        const response = await fetch(url);
        const result = await response.json();
        
        if (result.success) {
            const products = result.data.content || result.data;
            displayProducts(products);
        }
    } catch (error) {
        console.error('商品読み込みエラー:', error);
        showError('商品の読み込みに失敗しました');
    } finally {
        showLoading(false);
    }
    
    // フィルターボタンのアクティブ状態を更新
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');
}

// カテゴリー別商品読み込み
async function loadProductsByCategory(categoryId) {
    showLoading(true);
    
    try {
        const response = await fetch(`${API_BASE_URL}/products/category/${categoryId}`);
        const result = await response.json();
        
        if (result.success) {
            const products = result.data.content || result.data;
            displayProducts(products);
            
            // スクロール
            document.querySelector('.products').scrollIntoView({ behavior: 'smooth' });
        }
    } catch (error) {
        console.error('商品読み込みエラー:', error);
    } finally {
        showLoading(false);
    }
}

// 商品表示
function displayProducts(products) {
    const productList = document.getElementById('product-list');
    productList.innerHTML = '';
    
    if (!products || products.length === 0) {
        productList.innerHTML = '<p style="grid-column: 1/-1; text-align: center; padding: 3rem;">商品が見つかりませんでした</p>';
        return;
    }
    
    products.forEach(product => {
        const card = createProductCard(product);
        productList.appendChild(card);
    });
}

// 商品カード作成
function createProductCard(product) {
    const card = document.createElement('div');
    card.className = 'product-card';
    
    const isOnSale = product.salePrice && product.salePrice < product.price;
    const effectivePrice = isOnSale ? product.salePrice : product.price;
    const inStock = product.stockQuantity > 0;
    
    // 割引率計算
    let discountPercent = 0;
    if (isOnSale) {
        discountPercent = Math.round(((product.price - product.salePrice) / product.price) * 100);
    }
    
    card.innerHTML = `
        <div class="product-image" onclick="viewProduct(${product.id})">
            ${product.mainImageUrl ? `<img src="${product.mainImageUrl}" alt="${product.name}">` : '📦'}
        </div>
        <div class="product-info">
            <h3 class="product-name">${product.name}</h3>
            <p class="product-description">${product.shortDescription || product.description || ''}</p>
            
            <div class="product-price">
                <span class="current-price">¥${effectivePrice.toLocaleString()}</span>
                ${isOnSale ? `
                    <span class="original-price">¥${product.price.toLocaleString()}</span>
                    <span class="sale-badge">${discountPercent}% OFF</span>
                ` : ''}
            </div>
            
            <div class="product-meta">
                <div class="rating">
                    ⭐ ${product.averageRating || 0} (${product.reviewCount || 0})
                </div>
                <span class="stock-status ${!inStock ? 'out-of-stock' : ''}">
                    ${inStock ? `在庫: ${product.stockQuantity}` : '在庫切れ'}
                </span>
            </div>
            
            <button class="add-to-cart-btn" 
                    onclick="addToCart(${product.id}, '${product.name}', ${effectivePrice})"
                    ${!inStock ? 'disabled' : ''}>
                ${inStock ? '🛒 カートに追加' : '在庫切れ'}
            </button>
        </div>
    `;
    
    return card;
}

// 商品詳細表示
function viewProduct(productId) {
    window.location.href = `product-detail.html?id=${productId}`;
}

// カートに追加
function addToCart(productId, productName, price) {
    const existingItem = cart.find(item => item.productId === productId);
    
    if (existingItem) {
        existingItem.quantity++;
    } else {
        cart.push({
            productId: productId,
            name: productName,
            price: price,
            quantity: 1
        });
    }
    
    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartCount();
    
    // 通知
    showNotification(`${productName} をカートに追加しました`);
}

// カート数更新
function updateCartCount() {
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
        cart = JSON.parse(savedCart);
    }
    
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    document.getElementById('cart-count').textContent = totalItems;
}

// 商品検索
async function searchProducts() {
    const keyword = document.getElementById('search-input').value.trim();
    
    if (!keyword) {
        loadProducts('all');
        return;
    }
    
    showLoading(true);
    
    try {
        const response = await fetch(`${API_BASE_URL}/products/search?keyword=${encodeURIComponent(keyword)}`);
        const result = await response.json();
        
        if (result.success) {
            const products = result.data.content || result.data;
            displayProducts(products);
            
            // スクロール
            document.querySelector('.products').scrollIntoView({ behavior: 'smooth' });
        }
    } catch (error) {
        console.error('検索エラー:', error);
    } finally {
        showLoading(false);
    }
}

// Enter キーで検索
document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('search-input');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                searchProducts();
            }
        });
    }
});

// ローディング表示
function showLoading(show) {
    const loading = document.getElementById('loading');
    if (loading) {
        loading.style.display = show ? 'block' : 'none';
    }
}

// 通知表示
function showNotification(message) {
    // 簡易的な通知
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 80px;
        right: 20px;
        background: #10b981;
        color: white;
        padding: 1rem 2rem;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        z-index: 10000;
        animation: slideIn 0.3s ease;
    `;
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

// エラー表示
function showError(message) {
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 80px;
        right: 20px;
        background: #ef4444;
        color: white;
        padding: 1rem 2rem;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        z-index: 10000;
    `;
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    setTimeout(() => notification.remove(), 3000);
}

// アニメーション用CSS
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(400px);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);
